package com.gugu42.rcmod.client.utils.glutils;

import java.util.ArrayList;

import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3f;


public class IndexedModel
{

	private ArrayList<Vector3f> vertices;
	private ArrayList<Vector2f> texCoords;
	private ArrayList<Vector3f> normals;
	private ArrayList<Vector3f> tangents;
	private ArrayList<Integer> indices;
    private ArrayList<OBJLoader.OBJIndex> objindices;

	public IndexedModel()
	{
		vertices = new ArrayList<Vector3f>();
		texCoords = new ArrayList<Vector2f>();
		normals = new ArrayList<Vector3f>();
		tangents = new ArrayList<Vector3f>();
		indices = new ArrayList<Integer>();
		objindices = new ArrayList<OBJLoader.OBJIndex>();
	}

	public ArrayList<Vector3f> getPositions()
	{
		return vertices;
	}

	public ArrayList<Vector2f> getTexCoords()
	{
		return texCoords;
	}

	public ArrayList<Vector3f> getNormals()
	{
		return normals;
	}

	public ArrayList<Integer> getIndices()
	{
		return indices;
	}

	public ArrayList<Vector3f> getTangents()
	{
		return tangents;
	}

	public void toMesh(Mesh mesh)
	{
		ArrayList<Vertex> verticesList = new ArrayList<Vertex>();
		int n = Math.min(vertices.size(), Math.min(texCoords.size(), normals.size()));
		for(int i = 0; i < n; i++ )
		{
			Vertex vertex = new Vertex(vertices.get(i), 
			        texCoords.get(i), 
			        normals.get(i), new Vector3f());
			verticesList.add(vertex);
		}
		Integer[] indicesArray = indices.toArray(new Integer[0]);
		Vertex[] verticesArray = verticesList.toArray(new Vertex[0]);
		int[] indicesArrayInt = new int[indicesArray.length];
		for(int i = 0; i < indicesArray.length; i++ )
			indicesArrayInt[i] = indicesArray[i];
		mesh.vertices = verticesArray;
		mesh.indices = indicesArrayInt;
	}

	public void computeNormals()
	{
		for(int i = 0; i < indices.size(); i += 3)
		{
			int i0 = indices.get(i);
			int i1 = indices.get(i + 1);
			int i2 = indices.get(i + 2);

			Vector3f v = (Vector3f)vertices.get(i1).copy();
			v.sub(vertices.get(i0));
			Vector3f l0 = v;
			v = (Vector3f)vertices.get(i2).copy();
			v.sub(vertices.get(i0));
			Vector3f l1 = v;
			v = (Vector3f)l0.copy();
			v.cross(l1);
			Vector3f normal = v;

			v = (Vector3f)normals.get(i0).copy();
			v.add(normal);
			normals.set(i0, v);
			v = (Vector3f)normals.get(i1).copy();
            v.add(normal);
			normals.set(i1, v);
			v = (Vector3f)normals.get(i2).copy();
            v.add(normal);
			normals.set(i2, v);
		}

		for(int i = 0; i < normals.size(); i++ )
		{
			normals.get(i).normalize();
		}
	}

	public void computeTangents()
	{
		tangents.clear();
		for(int i = 0; i < vertices.size(); i++ )
			tangents.add(new Vector3f());

		for(int i = 0; i < indices.size(); i += 3)
		{
			int i0 = indices.get(i);
			int i1 = indices.get(i + 1);
			int i2 = indices.get(i + 2);

			Vector3f v = (Vector3f)vertices.get(i1).copy();
			v.sub(vertices.get(i0));
			Vector3f edge1 = v;
			v = (Vector3f)vertices.get(i2).copy();
			v.sub(vertices.get(i0));
			Vector3f edge2 = v;

			double deltaU1 = texCoords.get(i1).x - texCoords.get(i0).x;
			double deltaU2 = texCoords.get(i2).x - texCoords.get(i0).x;
			double deltaV1 = texCoords.get(i1).y - texCoords.get(i0).y;
			double deltaV2 = texCoords.get(i2).y - texCoords.get(i0).y;

			double dividend = (deltaU1 * deltaV2 - deltaU2 * deltaV1);
			double f = dividend == 0.0f ? 0.0f : 1.0f / dividend;

			Vector3f tangent = new Vector3f((float)(f * (deltaV2 * edge1.getX() - deltaV1 * edge2.getX())), (float)(f * (deltaV2 * edge1.getY() - deltaV1 * edge2.getY())), (float)(f * (deltaV2 * edge1.getZ() - deltaV1 * edge2.getZ())));

			v = (Vector3f)tangents.get(i0).copy();
			v.add(tangent);
			tangents.set(i0, v);
			v = (Vector3f)tangents.get(i1).copy();
			v.add(tangent);
			tangents.set(i1, v);
			v = (Vector3f)tangents.get(i2).copy();
			v.add(tangent);
			tangents.set(i2, v);
		}

		for(int i = 0; i < tangents.size(); i++ )
			tangents.get(i).normalize();
	}

    public ArrayList<OBJLoader.OBJIndex> getObjIndices()
    {
        return objindices;
    }

    public Vector3f computeCenter()
    {
        float x = 0;
        float y = 0;
        float z = 0;
        for(Vector3f position : vertices)
        {
            x += position.getX();
            y += position.getY();
            z += position.getZ();
        }
        x /= vertices.size();
        y /= vertices.size();
        z /= vertices.size();
        return new Vector3f(x, y, z);
    }
}
